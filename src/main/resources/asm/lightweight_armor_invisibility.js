function initializeCoreMod() {
    var ASM_HOOKS = "svenhjol/charm/base/CharmAsmHooks";
    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
    var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
    var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
    var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
    var IincInsnNode = Java.type('org.objectweb.asm.tree.IincInsnNode');
    var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
    var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
    var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
    var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');

    return {
        'lightweight_armor_invisibility_livingentity': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.LivingEntity',
                'methodName': 'func_213343_cS', // getArmorCoverPercentage
                'methodDesc': '()F'
            },
            transformer: function(method) {
                var success = false;
                var newInstructions = new InsnList();
                var arrayLength = method.instructions.size();

                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    if (instruction.getOpcode() == Opcodes.IINC) {
                        var label = new LabelNode();
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 5));
                        newInstructions.add(ASM.buildMethodCall(ASM_HOOKS, "isArmorInvisible", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Z", ASM.MethodType.STATIC))
                        newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                        newInstructions.add(new IincInsnNode(3, -1));
                        newInstructions.add(label);

                        method.instructions.insert(instruction, newInstructions);
                        success = true;
                        break;
                    }
                }

                print("[Charm ASM] (LightweightArmorInvisibility) " + (success ? "Patched LivingEntity" : "Failed to patch LivingEntity"));
                return method;
            }
        },
        'lightweight_armor_invisibility_bipedarmorlayer': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.layers.BipedArmorLayer',
                'methodName': 'func_241739_a_', // renderArmorPart
                'methodDesc': '(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/inventory/EquipmentSlotType;ILnet/minecraft/client/renderer/entity/model/BipedModel;)V'
            },
            transformer: function(method) {
                var success = false;
                var newInstructions = new InsnList();
                var arrayLength = method.instructions.size();

                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    if (instruction.getOpcode() == Opcodes.ASTORE) {
                        var label = new LabelNode();
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 7));
                        newInstructions.add(ASM.buildMethodCall(ASM_HOOKS, "isArmorInvisible", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Z", ASM.MethodType.STATIC))
                        newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                        newInstructions.add(new InsnNode(Opcodes.RETURN));
                        newInstructions.add(label);

                        method.instructions.insert(instruction, newInstructions);
                        success = true;
                        break;
                    }
                }

                print("[Charm ASM] (LightweightArmorInvisibility) " + (success ? "Patched BipedArmorLayer" : "Failed to patch BipedArmorLayer"));
                return method;
            }
        }
    }
}