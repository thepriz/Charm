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
        'beacons_heal_mobs': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.tileentity.BeaconTileEntity',
                'methodName': 'func_146000_x', // addEffectsToPlayers
                'methodDesc': '()V'
            },
            transformer: function(method) {
                var success = false;
                var newInstructions = new InsnList();
                var instruction = method.instructions.get(0);

                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/BeaconTileEntity", ASM.mapField("field_145850_b"), "Lnet/minecraft/world/World;")); // world
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/BeaconTileEntity", ASM.mapField("field_146012_l"), "I")); // levels
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/BeaconTileEntity", ASM.mapField("field_174879_c"), "Lnet/minecraft/util/math/BlockPos;")); // pos
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/BeaconTileEntity", ASM.mapField("field_146013_m"), "Lnet/minecraft/potion/Effect;")); // primaryEffect
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/BeaconTileEntity", ASM.mapField("field_146010_n"), "Lnet/minecraft/potion/Effect;"));
                newInstructions.add(ASM.buildMethodCall(ASM_HOOKS, "mobsInBeaconRange", "(Lnet/minecraft/world/World;ILnet/minecraft/util/math/BlockPos;Lnet/minecraft/potion/Effect;Lnet/minecraft/potion/Effect;)V", ASM.MethodType.STATIC));

                method.instructions.insertBefore(instruction, newInstructions);
                print("[Charm ASM] (BeaconsHealAnimals) " + (success ? "Patched BeaconTileEntity" : "Failed to patch BeaconTileEntity"));
                return method;
            }
        }
    }
}