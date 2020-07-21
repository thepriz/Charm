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
        'lantern_improvements': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraftforge.registries.GameData$BlockCallbacks',
                'methodName': 'onAdd', // this is a forge method so it's not obfuscated
                'methodDesc': '(Lnet/minecraftforge/registries/IForgeRegistryInternal;Lnet/minecraftforge/registries/RegistryManager;ILnet/minecraft/block/Block;Lnet/minecraft/block/Block;)V'
            },
            transformer: function(method) {
                var success = false;
                var newInstructions = new InsnList();
                var arrayLength = method.instructions.size();

                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    if (instruction.getOpcode() == Opcodes.IFNE) {
                        var label = new LabelNode();
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 4));
                        newInstructions.add(ASM.buildMethodCall(ASM_HOOKS, "bypassForgeStateCheck", "(Lnet/minecraft/block/Block;)Z", ASM.MethodType.STATIC))
                        newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                        newInstructions.add(new InsnNode(Opcodes.RETURN));
                        newInstructions.add(label);

                        method.instructions.insert(instruction, newInstructions);
                        method.instructions.remove(instruction);
                        success = true;
                        break;
                    }
                }

                print("[Charm ASM] (LanternImprovements) " + (success ? "Patched GameData$BlockCallbacks" : "Failed to patch GameData$BlockCallbacks"));
                return method;
            }
        }
    }
}